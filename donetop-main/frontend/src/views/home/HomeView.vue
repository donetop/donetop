<template>
  <div class="home">
    <div class="title">this page is home...</div>
    <tool-bar></tool-bar>
  </div>
</template>

<script>
import ToolBar from "@/components/ToolBar.vue";

export default {
  components: {
    ToolBar,
  },
};
</script>

<style lang="scss" scoped>
.home {
  .title {
    font-size: 30px;
  }
}
</style>
