<template>
  <!--公共底部-->
    <div class="public-bottom">
        <div>
            <p>小关盈利宝</p>
            <p>一个前后端分离的项目<a href="javascript:void(0);" @click="golink('/')">Visit the HomePage</a></p>
        </div>
    </div>
</template>

<script>
export default {
    name: "ProjectFoot",
    methods:{
        golink(url,params){
            this.$router.push({
                path:url,
                query:params
            })
        }

    },
    mounted() {

    }
}
</script>

<style scoped>

</style>