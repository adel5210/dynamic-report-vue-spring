<template>
  <b-navbar toggleable="lg" type="light" variant="light">
    <b-navbar-brand href="#">
      <img src="../assets/images/ooredoo_logo.jpg" class="d-inline-block align-top" v-b-toggle.sidebar-1 height="30px;">
    </b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav>
        <b-nav-form v-on:submit="onSearchSubmit">
          <b-form-input v-model="customerIds" size="sm" class="mr-sm-2" placeholder="Search for Customer ID"
                        style="width: 500px"></b-form-input>
          <b-button size="sm" class="my-2 my-sm-0" type="submit">Search</b-button>
          <b-form-select style="margin-left: 20px;"
                         v-model="reportSelection"
                         @change="updateReportSelection"
                         :options="reportLists"></b-form-select>
        </b-nav-form>
<!--        <b-navbar-nav>-->
<!--          <b-form-file-->
<!--              v-model="uploadedFile"-->
<!--              :state="Boolean(uploadedFile)"-->
<!--              placeholder="Choose a file or drop it here (CSV only)"-->
<!--              drop-placeholder="Drop file here (csv only)"-->
<!--          ></b-form-file>-->
<!--        </b-navbar-nav>-->
      </b-navbar-nav>
      <b-navbar-nav class="ml-auto">
        <b-nav-item right>Welcome, {{ currentUser ? currentUser.username : '' }}</b-nav-item>
<!--        <b-nav-item @click="onLogout" right>Logout</b-nav-item>-->
        <b-button @click="onLogout" variant="danger">Logout</b-button>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</template>

<script>
import CustomerReportDataService from "@/services/CustomerReportDataService";

export default {
  name: "NavTemplate",
  data() {
    return {
      customerIds: "",
      reportLists: [{
        value: null,
        text: '-- Please select a report --'
      }],
      customerResults: [],
      reportSelection: null,
      uploadedFile: null
    }
  },
  emits: ['customerResults', 'reportSelection'],
  methods: {
    onSearchSubmit(event) {
      event.preventDefault();
      let data = {
        customerIds: this.customerIds
      };
      CustomerReportDataService.searchCustomer(data)
          .then(response => {
            this.customerResults = response.data;
            this.$emit('customerResults', this.customerResults);
          });
    },
    updateReportSelection() {
      this.$emit('reportSelection', this.reportSelection);
    },
    onLogout() {
      this.$store.dispatch('auth/logout', this.user).then(() => {
        this.$router.push('login')
      }, error => {
        this.message = (error.response && error.response.data) || error.message || error.toString();
      });
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  beforeMount() {
    this.$forceUpdate();
  },
  mounted() {
    CustomerReportDataService.fetchReportList().then(response => {
      response.data.forEach(e => {
        this.reportLists.push({
          value: e.reportType,
          text: e.reportType
        })
      });
    });
  }
}
</script>

<style scoped>
body {
  background: #ffffff;
}
</style>