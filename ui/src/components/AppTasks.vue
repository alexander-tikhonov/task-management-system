<template>
    <div class="col-full">
        <b-alert show>
            <h1>Tasks</h1>
        </b-alert>
    </div>
</template>

<script>
import {AXIOS} from '../http-common'

export default {
    components: {
    },
    data () {
        return {

        }
    },
    computed: {},
    methods: {
        loadTasks() {
            AXIOS.get('/tasks').then(response => {
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