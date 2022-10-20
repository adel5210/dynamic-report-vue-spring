<template>
  <div style="height: 100%">
    <NavTemplate @customerResults="(res) => {this.customerBasicDetails = res;}"
                 @reportSelection="(res) => {this.reportSelection = res;}"
    />
    <div style="height: 100%">
      <b-row style="height: 100%">
        <b-col cols="2" style="background-color: red;">

        </b-col>
        <b-col cols="10">
          <div class="container" style="padding-top: 40px;">
            <b-button variant="primary" @click="fetchReportDetail(String(customerBasicDetails.map(m=>m.customerId)))"
                      v-if="customerBasicDetails.length > 0"
                      :disabled="null == reportSelection">Show all report</b-button>
            <b-card-group columns>
              <b-card :title="customerIdPrefix(customer.customerId)" v-for="customer in customerBasicDetails"
                      @click="fetchReportDetail(customer.customerId)"
                      :key="customer.id">
                <b-card-text>CustCode: {{ customer.id }}</b-card-text>
                <b-card-text>Name: {{ customer.name }}</b-card-text>
                <b-card-text>Civil ID: {{ customer.civilId }}</b-card-text>
                <b-card-text>MSISDN: {{ customer.msisdn }}</b-card-text>
                <b-button variant="primary" @click="fetchReportDetail(customer.customerId)"
                          :disabled="null == reportSelection">Show Report
                </b-button>
              </b-card>
            </b-card-group>

            <b-modal id="modal-config-report"
                     title="Additional details"
                     @hidden="resetModalDetail"
                     @ok="processOkModalDetail">
              <div v-for="modalDetail in modalInsertDetails" :key="modalDetail.position">

                <b-form inline @submit.stop.prevent="processSubmitModalDetail">
                  <label class="mr-sm-2" :for="modalDetail.parameterName">{{ modalDetail.parameterLabel }}</label>
                  <b-form-input
                      :id="modalDetail.parameterName"
                      v-model="modalDetail.parameterResult"
                      class="mb-2 mr-sm-2 mb-sm-0"
                      required></b-form-input>
                </b-form>
              </div>
            </b-modal>

            <b-modal id="modal-report-result" scrollable size="lg" :title="reportSelection">
              <div class="pdf-page">
                <pdf :src="fileResult"
                     ref="pdfComponent"
                     v-for="i in numPages"
                     :key="i"
                     :page="i"
                     style="height: 100%"
                ></pdf>
              </div>
              <template #modal-footer="{ ok }">
                <b-button @click="printReport()">
                  Print Report
                </b-button>
              </template>
            </b-modal>
          </div>
        </b-col>
      </b-row>
    </div>
  </div>
</template>
<script>

import CustomerReportDataService from "@/services/CustomerReportDataService";
import NavTemplate from "@/components/NavTemplate";
import pdf from "vue-pdf";
import '../assets/css/home.css'
import '../assets/css/pdf.css'

export default {
  components: {
    NavTemplate,
    pdf
  },
  data() {
    return {
      customerIds: "",
      customerBasicDetails: [],
      modalInsertDetails: [],
      reportSelection: null,
      selectedCustomer: null,
      fileResult: null,
      numPages: 0
    };
  },
  methods: {
    customerIdPrefix(str) {
      return "Cust ID: " + str;
    },
    camelCaseToWords(text) {
      const result = text.replace(/([A-Z])/g, " $1");
      return result.charAt(0).toUpperCase() + result.slice(1);
    },
    fetchReportDetail(customerId) {
      this.selectedCustomer = customerId;
      let reportsDto = {
        reportType: this.reportSelection
      };
      CustomerReportDataService.fetchReportParams(reportsDto).then(response => {
        this.modalInsertDetails = response.data;
        if (this.modalInsertDetails.length > 0) {
          this.$bvModal.show('modal-config-report');
        }else{
          this.processSubmitModalDetail();
        }
      }).catch(e => {
        console.log(e);
      });
    },
    resetModalDetail() {
      this.modalInsertDetails = [];
      this.selectedCustomer = null;
    },
    processOkModalDetail(event) {
      console.log(event);
      event.preventDefault();
      this.processSubmitModalDetail();
    },
    processSubmitModalDetail() {
      let customerIds = [this.selectedCustomer];
      let reportsDto = {
        reportType: this.reportSelection,
        reportParameters: this.modalInsertDetails
      }
      let data = {
        customerIds: customerIds,
        reportsDto: reportsDto
      }
      console.log(data);
      CustomerReportDataService.generateReport(data).then(response => {
        console.log(response);
        const blob = new Blob([response.data]);
        this.fileResult = pdf.createLoadingTask(URL.createObjectURL(blob));
        this.fileResult.promise.then(pdf => this.numPages = pdf.numPages);
        this.$bvModal.hide('modal-config-report');
        this.$bvModal.show('modal-report-result');
        this.resetModalDetail();
      }).catch(e => {
        console.log(e);
        this.fileResult = null;
      })
    },
    printReport(){
      console.log("printing report...");
      this.$refs.pdfComponent.forEach(p=>p.print(149));
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push('/login');
    }
  }
};
</script>