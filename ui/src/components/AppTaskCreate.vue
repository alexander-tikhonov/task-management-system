<template>
  <div class="col-full">
    <app-header></app-header>
    <b-container fluid class="tasks-header">
      <h3>Новая задача</h3>
      <b-row>
        <b-col cols="4">
          <form @submit.prevent>
            <div class="form-group">
              <label>Заголовок<span class="required"> *</span></label>
              <b-form-input v-model="taskForSave.title"></b-form-input>
            </div>

            <div class="form-group">
              <label>Описание задачи<span class="required"> *</span></label>
              <b-form-textarea id="textarea"
                               v-model="taskForSave.description"
                               rows="3"
                               max-rows="6"
              ></b-form-textarea>
            </div>

            <div class="form-group">
              <label for="assigneeSelect">Исполнитель<span class="required"> *</span></label>
              <v-select id="assigneeSelect" label="name"
                        v-model="searchAssigneeName"
                        :options="assignees"
                        @option:selected="setAssignee"
                        @search="onSearch">

                <template #no-options="">
                  Не удалось найти указанного исполнителя
                </template>
              </v-select>
            </div>

            <div class="form-group">
              <label>Приоритет<span class="required"> *</span></label>
              <b-form-select v-model="taskForSave.priority"
                             :options="priorities"></b-form-select>
            </div>

            <button :disabled="isButtonDisabled()" type="submit"
                    class="btn btn-primary float-sm-right" @click="saveTask">
              Сохранить
            </button>
          </form>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import AppHeader from "@/components/AppHeader";
import {AXIOS} from "@/http-common"
import {router} from '@/router'

export default {
  components: {
    AppHeader
  },
  data() {
    return {
      taskForSave: {},
      searchAssigneeName: "",
      assignees: [],
      priorities: [
        "LOW",
        "MEDIUM",
        "HIGH"
      ]
    }
  },
  methods: {
    saveTask() {
      AXIOS.post("/tasks", {
        title: this.taskForSave.title,
        description: this.taskForSave.description,
        assigneeId: this.taskForSave.assignee.id,
        priority: this.taskForSave.priority
      }).then((response) => {
        let savedTaskId = response.data.id
        router.push({ name: 'TaskDetail', params: { id: savedTaskId } })
      }).catch(error => {
        console.log('ERROR: ' + error.response.data)
      })
    },
    onSearch(search, loading) {
      if (search.length >= 3) {
        loading(true);
        this.search(loading, search);
      }
    },
    search(loading, search) {
      let request;
      request = AXIOS.get("/users?name=" + search);

      request.then(response => {
        this.assignees = response.data
      }).catch(() => {
        this.errorMessage = "Не удалось найти исполнителя";
      }).finally(() => loading(false));
    },
    setAssignee(selectedItem) {
      this.taskForSave.assignee = selectedItem;
    },
    isButtonDisabled() {
      let task = this.taskForSave;
      return task.title == null ||
          task.description == null ||
          task.assignee == null ||
          task.priority == null;
    }
  }
}
</script>

<style scoped>
.tasks-header {
  margin-top: 30px;
  margin-bottom: 15px;
}

.required {
  color: #0275d8;
}
</style>