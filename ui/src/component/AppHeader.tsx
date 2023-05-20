import {
    Code,
    Group, Header,
    Image,
    Text,
    useMantineTheme
} from '@mantine/core';
import { useState } from 'react';
import { UserButton } from './UserButton';

export default function AppHeader({ appinfo }) {

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
                    <Text<'a'> component="a" href={"/"} size={"xl"} style={{ fontWeight: "bold" }}>BuildGATE<Text size={'xs'} c="gray">version<Code sx={{ fontWeight: 700 }}>{appinfo?.build?.version}</Code></Text></Text>
                </Group>
                {/* <UserButton
                    image="https://media.licdn.com/dms/image/C5103AQFR8h2Px6ji0w/profile-displayphoto-shrink_400_400/0/1543131915690?e=1689206400&v=beta&t=Gr_bquXxzOf-zWNPgEGeN9PKzV5KIwpm6SNjGOKLjOo"
                    name="Vidya Sagar Gupta"
                    email="v3sagar@gmail.com"
                /> */}
            </div>
        </Header>
    );
}