import http from "../http-common";
import authHeader from "@/services/auth-header";

const KEY_TOKEN_PROP = 'user';
const user = JSON.parse(localStorage.getItem(KEY_TOKEN_PROP));

class CustomerReportDataService {

    fetchReportList(){
        return http.get("/report/lists", {
            headers: authHeader()
        });
    }

    fetchReportParams(data){
        return http.post("/report/params/ui", data, {
            headers: authHeader()
        });
    }

    searchCustomer(data){
        return http.post("/customers", data, {
            headers: authHeader()
        });
    }

    generateReport(data){
        return http.post("/report/generate", data, {
            responseType: "blob",
            headers:{
                Authorization: 'Bearer ' + user.accessToken
            }
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

}

export default new CustomerReportDataService();