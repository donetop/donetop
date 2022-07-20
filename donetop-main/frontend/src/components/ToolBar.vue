<template>
  <div class="header">
    <router-link to="/login">Login</router-link>
  </div>
</template>

<style lang="scss" scoped>
.header {
  color: white;
  background-color: #42b883;
  display: flex;
  padding: 8px;

  .router-link-exact-active {
    color: #35495e;
  }

  a {
    color: white;
  }
}
</style>
