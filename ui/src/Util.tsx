

export const getMainSocketUrl = '/ws-message';
export const actuatorUrl = `/actuator`
export const infoUrl = `${actuatorUrl}/info`;

/* export const getMainSocketUrl = () => {
    let referredInternal = true;   //Make it true for context referal
    let url = 'http://localhost:8080/ws-message';
    if (referredInternal) {
        url = '/ws-message';
    }
    return url;
} */