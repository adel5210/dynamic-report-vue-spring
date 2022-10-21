// Authentication service

import axios from "axios";

const API_URL = 'http://localhost:8187/api';
const KEY_TOKEN_PROP = 'user';

class AuthService{
    //login with JWT
    login(user){
        return axios.post(API_URL+'/auth/authenticate', {
            username: user.username,
            password: user.password
        }).then(response => {
            if(response.data.accessToken){
                localStorage.setItem(KEY_TOKEN_PROP, JSON.stringify(response.data));
            }
            return response.data;
        });
    }

    //logout
    logout(user){
        localStorage.removeItem(KEY_TOKEN_PROP);
        return user;
    }

}

export default new AuthService();