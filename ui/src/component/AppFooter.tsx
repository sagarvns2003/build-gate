import { createStyles, Footer, Text } from '@mantine/core';

const useStyles = createStyles((theme: any) => ({
    footer: {
        marginTop: 120,
        paddingTop: theme.spacing.xl * 2,
        paddingBottom: theme.spacing.xl * 2,
        backgroundColor: theme.colorScheme === 'dark' ? theme.colors.dark[6] : theme.colors.gray[0],
        borderTop: `1px solid ${theme.colorScheme === 'dark' ? theme.colors.dark[5] : theme.colors.gray[2]}`,
    }
}));

export default function AppFooter() {
    const { classes } = useStyles();

    return (
        <Footer height={60} p="md" className={classes.footer}>
            <Text color="dimmed" size="xs" align='right'>
                Copyright © 2022 Vidya Sagar Gupta | All rights reserved.
            </Text>
        </Footer>
    );
}