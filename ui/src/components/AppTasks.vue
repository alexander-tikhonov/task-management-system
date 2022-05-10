<template>
  <div class="col-full">
    <app-header></app-header>
  </div>
</template>

<script>
import AppHeader from "@/components/AppHeader";
import {AXIOS} from '@/http-common'
import {mapActions} from "vuex";

export default {
  components: {
    AppHeader
  },
  data() {
    return {}
  },
  computed: {},
  methods: {
    ...mapActions('user', {
      logout: 'logout'
    }),
    loadTasks() {
      AXIOS.get('/tasks/assignee').then(response => {
        console.log(response.data);
      }).catch(error => {
        console.log('ERROR: ' + error.response.data);
      });
    }
  },
  mounted() {
    this.loadTasks();
  }
}
</script>

<style>
</style>