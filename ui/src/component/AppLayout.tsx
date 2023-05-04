import { AppShell, Modal, Text, useMantineTheme } from '@mantine/core';
import { IconPlugConnectedX } from '@tabler/icons';
import { useState } from 'react';
import { Route, Routes } from 'react-router-dom';
import SockJsClient from 'react-stomp';
import AppFooter from './AppFooter';
import AppHeader from './AppHeader';
import Error from './Error';
import MainNavigationBar from "./MainNavigationBar";
import BuildConfigure from './navigation/build/Configure';
import BuildRun from './navigation/build/Run';
import BuildStatus from './navigation/build/Status';
import Dashboard from './navigation/Dashboard';
import {getMainSocketUrl} from '../Util';


export default function AppLayout() {
    const theme = useMantineTheme();
    const [message, setMessage] = useState(JSON.parse('{"statistics":{"runningCount":0,"queuedCount":0,"doneCount":0,"cancelledCount":0,"failedCount":0},"jobInformation":[]}'));
    const [error, setError] = useState();
    const [opened, setOpened] = useState(false);

    const statisticsData = message?.statistics;
    const jobInformation = message?.jobInformation;

    let onConnected = () => {
        console.log("Connected!!")
        setOpened(false);
        setError(undefined);
    }

    let onConnectionFailed = (err: any) => {
        console.log("onConnectionFailed... " + JSON.stringify(err))
        setError(err);
        setOpened(true);
    }

    let onMessageReceived = (msg: any) => {
        //console.log("onMessageReceived... " + JSON.stringify(msg))
        setMessage(msg);
        console.log("onMessageReceived... " + JSON.stringify(message))
    }

    return (
        <>
            <SockJsClient
                url={getMainSocketUrl}
                topics={['/topic/message']}
                onConnect={onConnected}
                onConnectFailure={(err: any) => onConnectionFailed(err)}
                onDisconnect={console.log("Disconnected!")}
                onMessage={(msg: any) => onMessageReceived(msg)}
                debug={false}
            />

            {/* Show model when connection error  */}
            <Modal title={"Connection error !"} closeOnEscape={false} closeOnClickOutside={false} withCloseButton={false}
                /* overlayColor={theme.colorScheme === 'dark' ? theme.colors.dark[9] : theme.colors.gray[2]} */
                /* overlayOpacity={0.55} */
                /* overlayBlur={3}  */opened={opened} onClose={() => setOpened(false)} >
                <Text size={'md'} color="red"><IconPlugConnectedX size={20} color="red" />   Unable to connect to server.</Text>
            </Modal>

            <AppShell
                styles={{
                    main: {
                        background: theme.colorScheme === 'dark' ? theme.colors.dark[8] : theme.colors.gray[0],
                    },
                }}
                navbarOffsetBreakpoint="sm"
                asideOffsetBreakpoint="sm"
                header={
                    <AppHeader />
                }
                navbar={
                    <MainNavigationBar />
                }
                footer={
                    <AppFooter />
                } >

                {/* Body here */}
                <Routes>
                    <Route path='/' element={<Dashboard />} />
                    <Route path='/dashboard' element={<Dashboard />} />
                    <Route path='/build/run' element={<BuildRun />} />
                    <Route path='/build/status' element={<BuildStatus />} />
                    <Route path='/build/status/:jobId' element={<BuildStatus />} />
                    <Route path='/build/configure' element={<BuildConfigure />} />
                    <Route path='/*' element={<Error />} />
                </Routes>

            </AppShell>
        </>
    );
}