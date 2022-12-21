<template>
  <div class="page-bar">
    <ul>
      <li v-if="page > 1"><a v-on:click="pageClickDe()">上一页</a></li>
      <li v-if="page == 1"><a class="banclick">上一页</a></li>
      <li
        v-for="index in indexs"
        v-bind:class="{ active: page == index }"
        :key="index"
      >
        <a v-on:click="btnClick(index)">{{ index }}</a>
      </li>
      <li v-if="page != all">
        <a v-on:click="pageClickAd()">下一页</a>
      </li>
      <li v-if="page == all"><a class="banclick">下一页</a></li>
      <li></li>
      <li v-if="page != all">
        <a v-on:click="overClick()">末尾</a>
      </li>
      <li v-if="page == all"><a class="banclick">末尾</a></li>
      <li>
        <a
          >共<i>{{ all }}</i
          >页</a
        >
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "PageBar",
  data() {
    return {};
  },
  props: ["all", "page"],
  computed: {
    indexs: function () {
      var left = 1;
      var right = this.all;
      var ar = [];
      if (this.all >= 5) {
        if (this.page > 3 && this.page < this.all - 2) {
          left = this.page - 2;
          right = this.page + 2;
        } else {
          if (this.page <= 3) {
            left = 1;
            right = 5;
          } else {
            right = this.all;
            left = this.all - 4;
          }
        }
      }
      while (left <= right) {
        ar.push(left);
        left++;
      }
      return ar;
    },
  },
  methods: {
    pageClickDe() {
      //   this.$emit('changePage',this.page-1)
      this.$parent.changePage(this.page - 1);
      this.$parent.find();
    },
    pageClickAd() {
      //   this.$emit('changePage',this.page+1)
      this.$parent.changePage(this.page + 1);
      this.$parent.find();
    },
    btnClick(index) {
      this.$parent.changePage(index);
      this.$parent.find();
    },
    overClick() {
      this.$parent.changePage(this.all);
      this.$parent.find();
    },
  },
};
</script>

<style>
.page-bar {
  margin: 40px auto;
  margin-top: 150px;
}
ul,
li {
  margin: 0px;
  padding: 0px;
}
li {
  list-style: none;
}
.page-bar li:first-child > a {
  margin-left: 0px;
}
.page-bar a {
  border: 1px solid #ddd;
  text-decoration: none;
  position: relative;
  float: left;
  padding: 6px 12px;
  margin-left: -1px;
  line-height: 1.42857143;
  color: #5d6062;
  cursor: pointer;
  margin-right: 20px;
}
.page-bar a:hover {
  background-color: #eee;
}
.page-bar a.banclick {
  cursor: not-allowed;
}
.page-bar .active a {
  color: #fff;
  cursor: default;
  background-color: #e96463;
  border-color: #e96463;
}
.page-bar i {
  font-style: normal;
  color: #d44950;
  margin: 0px 4px;
  font-size: 12px;
}
</style>
