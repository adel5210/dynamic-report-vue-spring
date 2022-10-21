<template>
  <div>
    <div class="container" style="margin-top: 100px; padding-left: 300px;padding-right: 300px;">
      <div class='ripple-background'>
        <div class='circle xxlarge shade1'></div>
        <div class='circle xlarge shade2'></div>
        <div class='circle large shade3'></div>
        <div class='circle mediun shade4'></div>
        <div class='circle small shade5'></div>
      </div>
      <div class="card">
        <div class="card-body">
          <h5 class="card-title">Customer Report</h5>
        </div>
        <b-form @submit.prevent="onSubmit" style="margin-left: 20px;margin-right:20px;margin-bottom: 20px;">
          <div class="form-group">
            <label for="username">Username</label>
            <input id="username" class="form-control" type="text" v-model="user.username" required/>
            <div v-if="errors.has('username')" class="alert alert-danger" role="alert">Username is required!</div>
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input id="password" class="form-control" type="password" v-model="user.password" required/>
            <div v-if="errors.has('password')" class="alert alert-danger" role="alert">Password is required!</div>
          </div>
          <div class="form-group">
            <div v-if="message" class="alert alert-danger" role="alert">{{ message }}</div>
          </div>
          <b-button type="submit" variant="danger" style="margin-top: 10px">Login</b-button>
        </b-form>
      </div>
    </div>
  </div>
</template>
<script>
import User from "../models/User"
import '../assets/css/login.css'

export default {
  name: "login",
  data() {
    return {
      user: new User('', ''),
      message: ''
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  created() {
    if (this.loggedIn) {
      this.$router.push('home');
    }
  },
  methods: {
    onSubmit(event) {
      event.preventDefault();
      this.$validator.validateAll().then(isValid => {
        if (!isValid) {
          return;
        }

        if (this.user.username && this.user.password) {
          this.$store.dispatch('auth/login', this.user).then(() => {
            this.$router.push('home');
          }, error => {
            this.message = (error.response && error.response.data) || error.message || error.toString();
          });
        }

      });
    },
  },
  beforeMount() {
    this.$forceUpdate();
  }
};
</script>
