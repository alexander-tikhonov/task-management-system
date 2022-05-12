<template>
  <div class="col-full">
    <h1>Не найдено</h1>
    <p>
      Запрашиваемая страница не найдена. Перейдите
      <router-link :to="{name: 'TasksAssignee'}">
        на главную
      </router-link>
    </p>
  </div>
</template>

<script>
export default {
  name: "AppNotFound"
}
</script>

<style scoped>
div {
  text-align: center;
}
</style>