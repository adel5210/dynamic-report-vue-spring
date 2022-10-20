import http from "../http-common";
import authService from "./auth-service"

const KEY_TOKEN_PROP = 'user';
const user = JSON.parse(localStorage.getItem(KEY_TOKEN_PROP));

class CustomerReportDataService {
    login(data){
        return authService.login(data);
    }

    logout(data){
        return authService.logout(data);
    }

    fetchReportList(){
        return http.get("/report/lists");
    }

    fetchReportParams(data){
        return http.post("/report/params/ui", data);
    }

    searchCustomer(data){
        return http.post("/customers", data);
    }

    generateReport(data){
        return http.post("/report/generate", data, {
            responseType: "blob"
        });
    }

    uploadCsv(file, onUploadProgress) {
        let formData = new FormData();
        formData.append("file", file);
        return http.post("/report/upload", formData, {
            headers: {
                Authorization: 'Bearer ' + user.accessToken,
                "Content-Type": "multipart/form-data"
            },
            onUploadProgress
        });
    }

    getFiles() {
        return http.get("/files");
    }

}

export default new CustomerReportDataService();