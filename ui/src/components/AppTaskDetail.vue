<template>
  <div class="col-full">
    <app-header></app-header>
    <b-container fluid class="tasks-header">
      <b-row>
        <b-col cols="8">
          <b-row>
            <b-col>
              <h3 v-if="!updates.titleChange">{{ currentTask.title }}</h3>
              <b-input-group v-else size="lg">
                <b-form-input @change="updateTask('title')" v-model="currentTask.title"></b-form-input>
              </b-input-group>
            </b-col>
            <b-col>
              <b-button v-show="isCreatedBy()" @click="onTitleChange" variant="outline-none"
                        class="update float-sm-right">
                <b-icon class="update" icon="pencil-fill"></b-icon>
              </b-button>
            </b-col>
          </b-row>
          <b-row>
            <b-col cols="2">
              <b-badge v-if="!updates.priorityChange" :variant="setPriorityIcon(currentTask.priority)">
                {{ currentTask.priority }}
              </b-badge>
              <b-form-select v-else v-model="currentTask.priority" :options="priorities"
                             @change="updateTask('priority')" size="sm"></b-form-select>
            </b-col>
            <b-col>
              <b-button v-show="isCreatedBy()" @click="onPriorityChange" variant="outline-none"
                        class="update-2 float-sm-left">
                <b-icon class="update" icon="pencil-fill"></b-icon>
              </b-button>
            </b-col>
          </b-row>

          <hr>
          <div>
            <b-button v-show="isCreatedBy()" @click="onDescriptionChange" variant="outline-none"
                      class="update float-sm-right">
              <b-icon class="update" icon="pencil-fill"></b-icon>
            </b-button>
            <p v-if="!updates.descriptionChange">{{ currentTask.description }}</p>
            <b-form-textarea
                v-else
                id="textarea"
                v-model="currentTask.description"
                v-bind:placeholder="currentTask.description"
                @change="updateTask('description')"
                rows="3"
                max-rows="6"
            ></b-form-textarea>
          </div>
        </b-col>
        <b-col>
          <b-card v-if="currentTask.createdBy">
            <div>
              <p>Поставил: {{ currentTask.createdBy.name }}</p>
            </div>
            <div>
              <b-row>
                <b-col>
                  <p>Исполнитель: {{ currentTask.assignee.name }}</p>
                  <div v-if="updates.assigneeChange" class="form-group">
                    <v-select label="name"
                              v-model="searchAssigneeName"
                              :options="assignees"
                              @option:selected="updateAssignee"
                              @search="onSearch">

                      <template #no-options="">
                        Не удалось найти указанного исполнителя
                      </template>
                    </v-select>
                  </div>
                </b-col>
                <b-col>
                  <b-button v-show="isCreatedBy()" @click="onAssigneeChange" variant="outline-none"
                            class="update-2 float-sm-left">
                    <b-icon class="update" icon="pencil-fill"></b-icon>
                  </b-button>
                </b-col>
              </b-row>
            </div>
            <div>
              <b-row>
                <b-col>
                  <p>Статус: {{ currentTask.status }}</p>
                  <b-form-select size="sm" v-if="updates.statusChange"
                                 v-model="currentTask.status"
                                 :options="statuses"
                                 @change="updateTask('status')"></b-form-select>
                </b-col>
                <b-col>
                  <b-button v-show="isCreatedBy() || isAssignee()" @click="onStatusChange" variant="outline-none"
                            class="update-2 float-sm-left">
                    <b-icon class="update" icon="pencil-fill"></b-icon>
                  </b-button>
                </b-col>
              </b-row>
            </div>
          </b-card>

          <div class="delete-btn">
            <b-button v-show="isCreatedBy()" @click="deleteTask" variant="danger"
                      class="float-sm-right">
              Удалить задачу
            </b-button>
          </div>
        </b-col>
      </b-row>
    </b-container>

    <b-container fluid class="tasks-comments">
      <p>Комментарии:</p>
      <b-row>
        <b-col cols="8">
          <b-card class="comment-item" v-for="(item, index) in comments" :key="index">
            <b-row>
              <b-col>
                <p class="comment-user">{{ item.user.name }}</p>
              </b-col>
              <b-col>
                <b-button v-show="isCommentOwner(item)" @click="deleteComment(item.id, index)" variant="outline-none"
                          class="delete-comment-btn float-sm-right">
                  <b-icon icon="x-lg"></b-icon>
                </b-button>
              </b-col>
            </b-row>
            <b-card-text>{{ item.content }}</b-card-text>
            <span class="comment-date">{{ item.createdAt }}</span>
          </b-card>
        </b-col>
      </b-row>
    </b-container>

    <b-container fluid class="send-comment">
      <b-row>
        <b-col cols="8">
          <b-form-textarea
              id="textarea"
              v-model="comment"
              placeholder="Комментарий..."
              rows="3"
              max-rows="6"
          ></b-form-textarea>
          <b-button class="comment-btn float-sm-right"
                    variant="outline-primary"
                    @click="sendComment"
                    :disabled="comment.length <= 0">Отправить
          </b-button>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import AppHeader from "@/components/AppHeader";
import {AXIOS} from "@/http-common";
import {mapGetters} from "vuex";
import {router} from "@/router";

export default {
  props: ['id'],
  components: {
    AppHeader
  },
  data() {
    return {
      currentTask: {},
      comments: [],
      comment: "",
      user: {},
      updates: {
        titleChange: false,
        priorityChange: false,
        descriptionChange: false,
        assigneeChange: false,
        statusChange: false
      },
      priorities: [
        "LOW",
        "MEDIUM",
        "HIGH"
      ],
      statuses: [
        "NEW",
        "CLOSED",
        "ANALYSIS",
        "DEVELOPMENT"
      ],
      searchAssigneeName: "",
      assignees: []
    }
  },
  methods: {
    ...mapGetters('user', {
      getUser: 'getUser'
    }),
    loadTask() {
      AXIOS.get('/tasks/' + this.id).then(response => {
        this.currentTask = response.data;
      }).catch(error => {
        console.log('ERROR: ' + error.response.data);
      });
    },
    loadComments() {
      AXIOS.get('/comments?taskId=' + this.id).then(response => {
        this.comments = response.data;
      }).catch(error => {
        console.log('ERROR: ' + error.response.data);
      });
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
    sendComment() {
      AXIOS.post('/comments', {
        content: this.comment,
        taskId: this.id
      }).then(response => {
        this.comments.push(response.data);
      }).catch(error => {
        console.log('ERROR: ' + error.response.data);
      }).finally(() => this.comment = "");
    },
    isCreatedBy() {
      if (this.currentTask.createdBy)
        return this.currentTask.createdBy.id === this.user.id;
    },
    isAssignee() {
      if (this.currentTask.assignee)
        return this.currentTask.assignee.id === this.user.id;
    },
    isCommentOwner(comment) {
      return comment.user.id === this.user.id
    },
    deleteComment(id, index) {
      AXIOS.delete('/comments/' + id).then(() => {
        this.comments.splice(index, 1);
      }).catch(error => {
        console.log('ERROR: ' + error.response.data);
      });
    },
    onTitleChange() {
      this.updates.titleChange = !this.updates.titleChange;
    },
    onPriorityChange() {
      this.updates.priorityChange = !this.updates.priorityChange;
    },
    onDescriptionChange() {
      this.updates.descriptionChange = !this.updates.descriptionChange;
    },
    onAssigneeChange() {
      this.updates.assigneeChange = !this.updates.assigneeChange;
    },
    onStatusChange() {
      this.updates.statusChange = !this.updates.statusChange;
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
    updateTask(element) {
      AXIOS.put('/tasks', {
        id: this.currentTask.id,
        title: this.currentTask.title,
        description: this.currentTask.description,
        assigneeId: this.currentTask.assignee.id,
        status: this.currentTask.status,
        priority: this.currentTask.priority
      }).then(response => {
        console.log(response.data)
        this.currentTask = response.data;
      }).catch(error => {
        console.log('ERROR: ' + error.response.data);
      }).finally(() => {
        if (element === 'title') {
          this.updates.titleChange = false
        } else if (element === 'priority') {
          this.updates.priorityChange = false
        } else if (element === 'description') {
          this.updates.descriptionChange = false
        } else if (element === 'assignee') {
          this.updates.assigneeChange = false
        } else if (element === 'status') {
          this.updates.statusChange = false
        }
      })
    },
    updateAssignee(selectedItem) {
      this.currentTask.assignee = selectedItem;
      this.updateTask('assignee');
    },
    deleteTask() {
      if (confirm("Подтвердите действие. Удалить задачу?")) {
        AXIOS.delete("/tasks/" + this.currentTask.id).then(() => {
          router.push({ name: 'TasksCreated' })
        }).catch(error => {
          console.log('ERROR: ' + error.response.data);
        })
      }
    }
  },
  mounted() {
    this.loadTask();
    this.loadComments();
    this.user = this.getUser();
  }
}
</script>

<style scoped>
.tasks-header {
  margin-top: 30px;
  margin-bottom: 15px;
}

.comment-item {
  margin-bottom: 15px;
}

.send-comment {
  margin-bottom: 50px;
}

.comment-user {
  font-size: 18px;
  color: #0275d8;
}

.comment-btn {
  margin-top: 15px;
}

.delete-comment-btn {
  font-size: 10px;
}

.comment-date {
  font-size: 15px;
}

.update {
  font-size: 15px;
  margin-left: 10px;
  color: #0275d8;
  cursor: pointer;
}

.update-2 {
  font-size: 11px;
  margin-left: 10px;
  color: #0275d8;
  cursor: pointer;
}

.delete-btn {
  margin-top: 10px;
}
</style>