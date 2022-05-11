<template>
  <div class="col-full">
    <app-header></app-header>
    <div class="tasks-header container-fluid">
      <h3>Поставленные задачи</h3>
    </div>

    <div class="container-fluid">
      <b-table class="task-table" striped hover
               show-empty empty-text="Нет записей для отображения" empty-filtered-text="Нет записей для отображения"
               :items="tasks"
               :fields="tableFields"
               @row-clicked="onRowClick">
        <template #head(status)="data">
          <div class="sort-header-items" @click="handleOrder('statusDirection')">{{ data.label }}</div>
        </template>
        <template #head(priority)="data">
          <div class="sort-header-items" @click="handleOrder('priorityDirection')">{{ data.label }}</div>
        </template>

        <template #cell(priority)="data">
          <div class="text-left">
            <b-badge :variant="setPriorityIcon(data.value)">{{ data.value }}</b-badge>
          </div>
        </template>
      </b-table>
    </div>
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
    return {
      tasks: [],
      filters: {
        statusDirection: null,
        priorityDirection: null
      },
      tableFields: [
        {
          key: "title",
          label: "Название"
        },
        {
          key: "assignee.name",
          label: "Исполнитель"
        },
        {
          key: "status",
          label: "Статус"
        },
        {
          key: "priority",
          label: "Приоритет"
        }
      ]
    }
  },
  computed: {},
  methods: {
    ...mapActions('user', {
      logout: 'logout'
    }),
    loadTasks() {
      let params = [];
      if (this.filters.statusDirection != null) {
        params.push('statusDirection=' + this.filters.statusDirection)
      }

      if (this.filters.priorityDirection != null) {
        params.push('priorityDirection=' + this.filters.priorityDirection)
      }

      AXIOS.get('/tasks/createdBy?' + params.join('&')).then(response => {
        this.tasks = response.data;
      }).catch(error => {
        console.log('ERROR: ' + error.response.data);
      });
    },
    handleOrder(field) {
      if (this.filters[field] === 'ASC') {
        this.filters[field] = 'DESC'
        this.loadTasks()
      } else if(this.filters[field] === 'DESC') {
        this.filters[field] = 'ASC'
        this.loadTasks()
      } else {
        this.filters[field] = 'ASC'
        this.loadTasks()
      }
    },
    setPriorityIcon(item) {
      if (item === "LOW") {
        return "primary"
      } else if (item === "MEDIUM") {
        return "warning"
      } else {
        return "danger"
      }
    },
    onRowClick(record) {
      console.log(record);
    }
  },
  mounted() {
    this.loadTasks();
  }
}
</script>

<style>
.tasks-header {
  margin-top: 30px;
  margin-bottom: 15px;
}

.sort-header-items {
  cursor: pointer;
  color: #0275d8
}

.task-table tr {
  cursor: pointer;
}
</style>