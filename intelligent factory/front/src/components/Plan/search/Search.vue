<template>
  <div id="Search">
    <div>
      <slot></slot>

      

      <slot name="button"></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "Search",
  data() {
    return {
      begindate: {
        year: "",
        month: "",
        day: "",
      },
      overdate: {
        year: new Date().getFullYear(),
        month: new Date().getMonth() + 1,
        day: new Date().getDate(),
      },
      result: {},
    };
  },
  computed: {
    beginTime: {
      get() {
        
          
            return (
              this.begindate.year +
              "-" +
              this.begindate.month.toString().padStart(2, "0") +
              "-" +
              this.begindate.day.toString().padStart(2, "0")
            )
          
          
        
      },
      set(val) {
        let re = val.split("-");
        this.begindate.year = re[0];
        this.begindate.month = Number(re[1]);
        this.begindate.day = Number(re[2]);
      },
    },
    overTime: {
      get() {
        return (
          this.overdate.year +
          "-" +
          this.overdate.month.toString().padStart(2, "0") +
          "-" +
          this.overdate.day.toString().padStart(2, "0")
        );
      },

      set(val) {
        let re = val.split("-");
        this.overdate.year = re[0];
        this.overdate.month = Number(re[1]);
        this.overdate.day = Number(re[2]);
      },
    },
  },
  methods: {},
};
</script>

<style>
</style>

