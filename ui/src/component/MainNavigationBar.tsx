import { Navbar, ScrollArea, createStyles, rem, useMantineTheme } from '@mantine/core';
import {
  IconAdjustments,
  IconCalendarStats,
  IconFileAnalytics,
  IconLayoutDashboard,
  IconLock,
  IconNotes,
  IconPresentationAnalytics,
} from '@tabler/icons';
import { useState } from "react";

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
    borderBottom: `${rem(1)} solid ${theme.colorScheme === 'dark' ? theme.colors.dark[4] : theme.colors.gray[3]
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

export default function MainNavigationBar({ appinfo }) {

  const { classes } = useStyles();
  const links = menuData.map((item) => <LinksGroup {...item} key={item.label} />);
  const theme = useMantineTheme();
  const [opened, setOpened] = useState(false);

  return (
    <Navbar p="xs" hiddenBreakpoint="sm" hidden={!opened} width={{ base: 300 }} style={{ paddingLeft: '25px', paddingRight: '26px' }}>

      <Navbar.Section className={classes.user} >
        <UserButton
          image="https://media.licdn.com/dms/image/C5103AQFR8h2Px6ji0w/profile-displayphoto-shrink_400_400/0/1543131915690?e=1689206400&v=beta&t=Gr_bquXxzOf-zWNPgEGeN9PKzV5KIwpm6SNjGOKLjOo"
          name="Vidya Sagar Gupta"
          email="v3sagar@gmail.com"
        />
      </Navbar.Section>

      <Navbar.Section grow className={classes.links} component={ScrollArea}>
        <div className={classes.linksInner}>{links}</div>
      </Navbar.Section>

    </Navbar>
  );
}