import { ActionIcon, Badge, Card, Container, createStyles, Group, Loader, Menu, Notification, Paper, Progress, ScrollArea, Table, Text } from '@mantine/core';
import { IconAlertTriangle, IconCircleCheck, IconClock, IconDots, IconEye, IconFileZip, IconRun, IconTrash, IconX } from '@tabler/icons';
import dayjs from 'dayjs';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import SockJsClient from 'react-stomp';
import { JOB_STATUS } from '../util/BuildStatus';
import {getMainSocketUrl} from '../../Util';

const useStyles = createStyles((theme) => ({

    mainContainer: {
        paddingLeft: '0px',
        paddingRight: '0px',
        maxWidth: '100%'
    },

    root: {
        backgroundImage: `linear-gradient(-60deg, ${theme.colors[theme.primaryColor][4]} 0%, ${theme.colors[theme.primaryColor][7]
            } 100%)`,
        padding: theme.spacing.xl,
        borderRadius: theme.radius.md,
        display: 'flex',
        /* maxWidth: 580, */
        [theme.fn.smallerThan('xs')]: {
            flexDirection: 'column',
        },
    },

    icon: {
        marginLeft: 'auto',
        marginRight: 'auto',
        marginTop: theme.spacing.lg,
        color: theme.colors[theme.primaryColor][6],
    },

    stat: {
        minWidth: 98,
        paddingTop: theme.spacing.xl,
        minHeight: 140,
        display: 'flex',
        flex: 1,
        flexDirection: 'column',
        justifyContent: 'space-between',
        backgroundColor: theme.white,
    },

    label: {
        textTransform: 'uppercase',
        fontWeight: 700,
        fontSize: theme.fontSizes.xs,
        fontFamily: `Greycliff CF, ${theme.fontFamily}`,
        color: theme.colors.gray[6],
        lineHeight: 1.2,
    },

    value: {
        fontSize: theme.fontSizes.lg,
        fontWeight: 700,
        color: theme.black,
    },

    count: {
        color: theme.colors.gray[6],
    },

    todayText: {
        fontSize: theme.fontSizes.sm,
        color: theme.white,
        lineHeight: 1,
        textAlign: 'center',
    },

    day: {
        fontSize: 44,
        fontWeight: 700,
        color: theme.white,
        lineHeight: 1,
        textAlign: 'center',
        marginBottom: 5,
        fontFamily: `Greycliff CF, ${theme.fontFamily}`,
    },

    month: {
        fontSize: theme.fontSizes.sm,
        fontWeight: 'bold',
        color: theme.white,
        lineHeight: 1,
        textAlign: 'center',
    },

    controls: {
        display: 'flex',
        flexDirection: 'column',
        marginRight: '18px' /* Number(theme.spacing.xl) * 2 */,

        [theme.fn.smallerThan('xs')]: {
            flexDirection: 'row',
            alignItems: 'center',
            marginRight: 0,
            marginBottom: theme.spacing.xl,
        },
    },

    date: {
        flex: 1,
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
    },

    control: {
        height: 28,
        width: '100%',
        color: theme.colors[theme.primaryColor][2],
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: theme.radius.md,
        transition: 'background-color 50ms ease',

        [theme.fn.smallerThan('xs')]: {
            height: 34,
            width: 34,
        },

        '&:hover': {
            backgroundColor: theme.colors[theme.primaryColor][5],
            color: theme.white,
        },
    },

    controlIcon: {
        [theme.fn.smallerThan('xs')]: {
            transform: 'rotate(-90deg)',
        },
    },

    progressBar: {
        '&:not(:first-of-type)': {
            borderLeft: `3px solid ${theme.colorScheme === 'dark' ? theme.colors.dark[7] : theme.white}`,
        },
    },

    status: {
        textTransform: 'uppercase',
        color: "#495057"
    },
    queued: {
        color: "#495057"
    },
    done: {
        color: "#089369"
    },
    failed: {
        color: "#d20e0e"
    },

}));

const statusColors: Record<string, string> = {
    queued: 'blue',
    done: 'green',
    cancelled: 'orange',
    failed: 'red'
};

const buildData = [
    {
        "index": "12345",
        "title": "dbs-ice-integration-service",
        "startTime": "09:10 AM",
        "duration": 55,
        "status": {
            "buildStatus": "running",
            "positive": 90,
            "negative": 10
        }
    },
    {
        "index": "112344",
        "title": "dbs-ice-integration-service",
        "startTime": "09:10 AM",
        "duration": 55,
        "status": {
            "buildStatus": "running",
            "positive": 70,
            "negative": 30
        }
    },
    {
        "index": "552345",
        "title": "dbs-ice-integration-service",
        "startTime": "-",
        "duration": 0,
        "status": {
            "buildStatus": "queued",
            "positive": 0,
            "negative": 0
        }
    },
    {
        "index": "123459",
        "title": "dbs-ice-integration-service",
        "startTime": "-",
        "duration": 0,
        "status": {
            "buildStatus": "queued",
            "positive": 0,
            "negative": 0
        }
    },
    {
        "index": "123456",
        "title": "dbs-ice-integration-service",
        "startTime": "09:10 AM",
        "duration": 55,
        "status": {
            "buildStatus": "done",
            "positive": 0,
            "negative": 0
        }
    },
    {
        "index": "123458",
        "title": "dbs-ice-integration-service",
        "startTime": "09:10 AM",
        "duration": 55,
        "status": {
            "buildStatus": "cancelled",
            "positive": 0,
            "negative": 0
        }
    },
    {
        "index": "123458",
        "title": "dbs-ice-integration-service",
        "startTime": "09:10 AM",
        "duration": 55,
        "status": {
            "buildStatus": "failed",
            "positive": 0,
            "negative": 0
        }
    }
];

export default function Dashboard() {
    const navigate = useNavigate();
    const { classes, theme } = useStyles();
    const [date, setDate] = useState(new Date());
    const [message, setMessage] = useState(JSON.parse('{"statistics":{"runningCount":0,"queuedCount":0,"doneCount":0,"cancelledCount":0,"failedCount":0},"jobInformation":[]}'));
    const [error, setError] = useState();

    const statisticsData = message?.statistics;
    const jobInformation = message?.jobInformation;


    let onConnected = () => {
        console.log("Connected!!")
        setError(undefined);
    }

    let onConnectionFailed = (err: any) => {
        console.log("onConnectionFailed... " + JSON.stringify(err))
        setError(err);
    }

    let onMessageReceived = (msg: any) => {
        //console.log("onMessageReceived... " + JSON.stringify(msg))
        setMessage(msg);
        console.log("onMessageReceived... " + JSON.stringify(message))
    }



    //Prepare stats data
    const statsData = [
        { icon: IconRun, label: 'In Progress', count: statisticsData?.runningCount },
        { icon: IconClock, label: 'Queued', count: statisticsData?.queuedCount },
        { icon: IconCircleCheck, label: 'Done', count: statisticsData?.doneCount },
        { icon: IconCircleCheck, label: 'Cancelled', count: statisticsData?.cancelledCount },
        { icon: IconAlertTriangle, label: 'Failed', count: statisticsData?.failedCount },
    ];
    const stats = statsData.map((stat) => (
        <Paper className={classes.stat} radius="md" shadow="md" p="xs" key={stat.label}>
            <stat.icon size={32} className={classes.icon} stroke={1.5} />
            <div>
                <Text className={classes.label}>{stat.label}</Text>
                <Text size="xs" className={classes.count}>
                    <span className={classes.value}>{stat.count}</span> JOB
                </Text>
            </div>
        </Paper>
    ));

    //const rows = buildData.map((row) => {
    const rows = jobInformation.map((row: any) => {

        let submitDate = dayjs(row.submitDate).format("hh:mm:ss A").toString();
        let startDate = row.startDate ? dayjs(row.startDate).format("hh:mm:ss A").toString() : "-";

        let totalReviews = 0, positiveReviews = 0, negativeReviews = 0;
        //console.log(row.status);
        //console.log(BUILD_STATUS.RUNNING);
        /* if (row.status === BUILD_STATUS.RUNNING) {
             totalReviews = row.status.negative + row.status.positive;
             positiveReviews = (row.status.positive / totalReviews) * 100;
             negativeReviews = (row.status.negative / totalReviews) * 100;
         }*/

        return (
            <tr key={row.jobId} id={row.jobId} onClick={(event) => navigate("/build/status/" + event.currentTarget.id)} style={{ cursor: "pointer" }}>
                {/* <td> <Anchor<'a'> id={row.jobId} size="sm">{row.jobId}</Anchor> </td> */}
                <td><Text size="md" weight={500} color={'#0d6dc1'}>{row.jobName}</Text></td>
                <td><Text size="sm">{submitDate}</Text></td>
                <td><Text size="sm">{startDate}</Text></td>
                <td><Text size="sm">{row.runningDuration}</Text></td>
                <td>
                    {row.status && row.status.toLowerCase() === JOB_STATUS.RUNNING
                        ? <>
                            <Group position="apart">
                                <Text size="xs" color="teal" weight={700}>
                                    {positiveReviews.toFixed(0)}% &nbsp; <Loader size="sm" variant="dots" color="green" />
                                </Text>
                            </Group>
                            <Progress classNames={{ bar: classes.progressBar }} value={75} label="75%" size="xl" radius="xl" />
                          </>
                        : <Badge color={statusColors[row.status.toLowerCase()]} variant={theme.colorScheme === 'dark' ? 'light' : 'outline'} >
                            {row.status}
                          </Badge>
                    }
                </td>
            </tr>
        );
    });

    // console.log("rows: " + rows?.length);

    return (
        <Container className={classes.mainContainer}>

            <SockJsClient
                url={getMainSocketUrl}
                topics={['/topic/message']}
                onConnect={onConnected}
                onConnectFailure={(err: any) => onConnectionFailed(err)}
                onDisconnect={console.log("Disconnected!")}
                onMessage={(msg: any) => onMessageReceived(msg)}
                debug={false}
            />
            {/* <div>{JSON.stringify(message)}</div>
            <br /> */}

            {error && error !== ""
                ? <><Notification icon={<IconX size={18} />} color="red" title="Connection error">{error}</Notification><br /></>
                : ""
            }

            <div className={classes.root}>
                <div className={classes.controls}>
                    <div className={classes.date}>
                        <Text size="md" className={classes.todayText}>Today</Text>
                        <Text className={classes.day}>{dayjs(date).format('DD')}</Text>
                        <Text className={classes.month}>{dayjs(date).format('MMMM')}</Text>
                    </div>
                </div>
                <Group sx={{ flex: 1 }}>{stats}</Group>
            </div>

            <br /><br />
            <Card withBorder shadow="sm" radius="md">
                <Card.Section withBorder inheritPadding py="xs">
                    <Group position="apart">
                        <Text weight={500}>Job Queue</Text>
                        <Menu withinPortal position="bottom-end" shadow="sm">
                            <Menu.Target>
                                <ActionIcon>
                                    <IconDots size={16} />
                                </ActionIcon>
                            </Menu.Target>

                            <Menu.Dropdown>
                                <Menu.Item icon={<IconFileZip size={14} />}>Download zip</Menu.Item>
                                <Menu.Item icon={<IconEye size={14} />}>Preview all</Menu.Item>
                                <Menu.Item icon={<IconTrash size={14} />} color="red">
                                    Delete all
                                </Menu.Item>
                            </Menu.Dropdown>
                        </Menu>
                    </Group>
                </Card.Section>
                {rows?.length > 0
                    //If jobs are available
                    ? <ScrollArea>
                        <Table sx={{ minWidth: 800 }} verticalSpacing="sm" highlightOnHover={true} frame={true}>
                            <thead>
                                <tr>
                                    <th style={{ width: "400px" }}>Name</th>
                                    <th style={{ width: "130px" }}>Submit Time</th>
                                    <th style={{ width: "130px" }}>Start Time</th>
                                    <th style={{ width: "130px" }}>Duration</th>
                                    <th style={{ width: "250px" }}>Status</th>
                                </tr>
                            </thead>
                            <tbody>{rows}</tbody>
                        </Table>
                    </ScrollArea>

                    //if jobs are not available
                    : <Text mt="sm" color="dimmed" size="sm">
                        No job in the queue to run! {' '}
                        <Text component="span" inherit color="blue">
                            You can goto RUN menu and schedule a job.
                        </Text>
                    </Text>
                }
            </Card>

        </Container>
    );


}