<template>
  <div>
    <keep-alive>
      <baseview></baseview>
    </keep-alive>
  </div>
</template>

<script>
import {request} from "@/network/request";
import baseview from "@/components/baseShop/baseview";
export default {
  name: "theWorkshopMain",
  components:{
    baseview
  },
  data() {
    return {
      workshopItem: ''
    }
  },
  created() {
    // this.getWorkShopItem()
  },
  methods: {
    // getWorkShopItem() {
  //     request({
  //       url: 'data/workshopIncre',
  //       methods: 'get'
  //     }).then(res => {
  //       console.log('yes');
  //       console.log(res.data.data);
  //       this.workshopItem = res.data.data.workshopItem;
  //     }).catch(err => {
  //       console.log('no');
  //       console.log(err);
  //     })
  //   }
  }

}
</script>

<style scoped>

</style>