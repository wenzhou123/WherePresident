<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
    <div class="wrapper">
      <nav>
        <RouterLink to="/">Home</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
</template>

<style>
body {
  margin: 0;
  padding: 0;
  background-color: #1e1e1e;
}
</style>

<style scoped>
</style>
