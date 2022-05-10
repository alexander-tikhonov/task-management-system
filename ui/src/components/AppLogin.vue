<template>
  <div class="login-background">
    <div class="container-fluid">
      <b-card class="col-sm-8 col-md-6 col-lg-4 login-form">
        <form @submit.prevent>
          <div class="form-group">
            <label for="login">Логин</label>
            <input type="text" class="form-control" id="login" v-model="userData.login" @keyup.enter="login(userData)">
          </div>
          <div class="form-group">
            <label for="password">Пароль</label>
            <input type="password" class="form-control" id="password" v-model="userData.password"
                   @keyup.enter="login(userData)">
          </div>
          <div class="alert alert-danger" role="alert" v-if="showError">
            {{ error.message }}
          </div>

          <div class="form-group">
            <button type="button" class="btn btn-primary btn-block" :disabled="isEmpty" @click="login(userData)">Войти
            </button>
          </div>
        </form>
      </b-card>
    </div>
  </div>
</template>

<script>
import {mapState, mapActions} from 'vuex'

export default {
  name: "AppLogin",
  data() {
    return {
      userData: {
        login: '',
        password: ''
      }
    }
  },
  computed: {
    ...mapState('user', {
      error: 'loginError'
    }),
    isEmpty() {
      return !this.userData.login || !this.userData.password
    },
    showError() {
      return this.error.message !== undefined
    }
  },
  methods: {
    ...mapActions('user', {
      login: 'login'
    })
  }
}
</script>

<style scoped>
.login-form {
  margin: 30px auto auto;
}

.login-background {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: white;
}

.divider-with-text {
  text-align: center;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  line-height: 0.1em;
  margin: 2em 0;
}

.divider-with-text span {
  background-color: white;
  padding: 0 10px;
}
</style>