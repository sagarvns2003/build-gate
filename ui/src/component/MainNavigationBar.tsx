import { useState } from "react";
import { Navbar, useMantineTheme, Group, Code, ScrollArea, createStyles, rem } from '@mantine/core';
import {
  IconNotes,
  IconCalendarStats,
  IconLayoutDashboard,
  IconPresentationAnalytics,
  IconFileAnalytics,
  IconAdjustments,
  IconLock,
} from '@tabler/icons';

import { LinksGroup } from "./NavbarLinksGroup";
import { UserButton } from "./UserButton";

const menuData = [
  { label: 'Dashboard', icon: IconLayoutDashboard, link: '/' },
  {
    label: 'Build',
    icon: IconNotes,
    initiallyOpened: true,
    links: [
      { label: 'Run', link: '/build/run' },
      { label: 'Configure', link: '/build/configure' }
    ],
  },
  {
    label: 'Releases',
    icon: IconCalendarStats,
    links: [
      { label: 'Upcoming releases', link: '/' },
      { label: 'Previous releases', link: '/' },
      { label: 'Releases schedule', link: '/' },
    ],
  },
  { label: 'Analytics', icon: IconPresentationAnalytics },
  { label: 'Contracts', icon: IconFileAnalytics },
  { label: 'Settings', icon: IconAdjustments },
  {
    label: 'Security',
    icon: IconLock,
    links: [
      { label: 'Enable 2FA', link: '/' },
      { label: 'Change password', link: '/' },
      { label: 'Recovery codes', link: '/' },
    ],
  },
];

const useStyles = createStyles((theme) => ({
  navbar: {
    backgroundColor: theme.colorScheme === 'dark' ? theme.colors.dark[6] : theme.white,
    paddingBottom: 0,
  },

  header: {
    padding: theme.spacing.md,
    paddingTop: 0,
    marginLeft: `calc(${theme.spacing.md} * -1)`,
    marginRight: `calc(${theme.spacing.md} * -1)`,
    color: theme.colorScheme === 'dark' ? theme.white : theme.black,
    borderBottom: `${rem(1)} solid ${
      theme.colorScheme === 'dark' ? theme.colors.dark[4] : theme.colors.gray[3]
    }`,
  },

  links: {
    marginLeft: `calc(${theme.spacing.md} * -1)`,
    marginRight: `calc(${theme.spacing.md} * -1)`,
  },

  linksInner: {
    paddingTop: theme.spacing.xl,
    paddingBottom: theme.spacing.xl,
  },

  user: {
    marginLeft: -theme.spacing.md,
    marginRight: -theme.spacing.md,
    borderBottom: `1px solid ${theme.colorScheme === 'dark' ? theme.colors.dark[4] : theme.colors.gray[3]}`,
  },
}));

export default function MainNavigationBar() {

  const { classes } = useStyles();
  const links = menuData.map((item) => <LinksGroup {...item} key={item.label} />);
  const theme = useMantineTheme();
  const [opened, setOpened] = useState(false);

  return (
    <Navbar p="md" hiddenBreakpoint="sm" hidden={!opened} width={{ sm: 200, lg: 300 }} style={{ paddingLeft: '16px', paddingRight: '16px' }}>

      <Navbar.Section className={classes.user} >
        <UserButton
          image="https://media-exp1.licdn.com/dms/image/C5103AQFR8h2Px6ji0w/profile-displayphoto-shrink_400_400/0/1543131915690?e=1667433600&v=beta&t=KK24DOUUPJXSdphMqeditJKZ_d6R8nSbBO1TszgLqq4&auto=format&fit=crop&w=255&q=80"
          name="Vidya Sagar Gupta"
          email="v3sagar@gmail.com"
        />
      </Navbar.Section>

      <Navbar.Section grow className={classes.links} component={ScrollArea}>
        <div className={classes.linksInner}>{links}</div>
      </Navbar.Section>


      <Navbar.Section className={classes.header}>
        <Group position="apart">
          {/* <Logo width={120} /> */}
          <Code sx={{ fontWeight: 700 }}>v3.1.2</Code>
        </Group>
      </Navbar.Section>


    </Navbar>
  );
}