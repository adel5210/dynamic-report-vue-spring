export default function authHeader(){
    const KEY_TOKEN_PROP = 'user';
    const user = JSON.parse(localStorage.getItem(KEY_TOKEN_PROP));

    if(user && user.accessToken){
        return {
            Authorization: 'Bearer ' + user.accessToken,
            "Content-type": "application/json"
        };
    }else {
        return {}
    }

}