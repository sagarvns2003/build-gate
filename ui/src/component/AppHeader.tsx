import {
    Text, Burger, Group, Header, useMantineTheme, Image
} from '@mantine/core';
import { useState } from 'react';

export default function AppHeader() {

    const [opened, setOpened] = useState(false);
    const theme = useMantineTheme();

    return (
        <Header height={70} p="md">
            <div style={{ display: 'flex', alignItems: 'center', height: '100%' }}>
                <Group>
                    {/* <Burger style={{ marginRight: '0px' }}
                        opened={opened}
                        onClick={() => setOpened((o) => !o)}
                        size="sm"
                        color={theme.colors.gray[6]}
                        mr="xl"
                    /> */}
                    <Image src={"../../logo.png"} height={24} width={24} />
                    <Text<'a'> component="a" href={"/"} size={"xl"} style={{ fontWeight: "bold" }}>BuildGATE</Text>
                </Group>
            </div>
        </Header>
    );
}