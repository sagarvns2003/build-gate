import { MantineProvider } from "@mantine/core";
import { theme } from "./theme";
import AppLayout from "./component/AppLayout";


export default function App() {
  return (
    <MantineProvider theme={theme}>
      <AppLayout />
    </MantineProvider>
  );
}