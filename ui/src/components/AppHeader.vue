<template>
  <div>
    <b-navbar toggleable="lg" type="light" variant="light">
      <b-navbar-brand :to="{name: 'Tasks'}">
        <h2 class="logo">Task Management System</h2>
      </b-navbar-brand>
      <b-navbar-nav class="ml-auto">
        <b-nav-form>
          <b-nav-item class="mr-sm-2">
            {{ user.name }}
          </b-nav-item>
          <b-button variant="outline-primary" class="my-2 my-sm-0" @click="logout()">Выход</b-button>
        </b-nav-form>
      </b-navbar-nav>
    </b-navbar>
  </div>
</template>

<script>
import {mapState, mapActions} from 'vuex'

export default {
  name: "AppHeader",
  data() {
    return {
    }
  },
  methods: {
    ...mapActions('user', {
      logout: 'logout'
    })
  },
  computed: {
    ...mapState('user', {
      user: 'user'
    }),
  },
}
</script>

<style scoped>
  .logo {
    color: #0275d8;
  }
</style>