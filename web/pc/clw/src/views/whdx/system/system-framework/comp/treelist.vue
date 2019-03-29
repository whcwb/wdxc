<style>
</style>
<template>
      <div>
            {{treeMess.title}}
            <br/>
            <div v-if="treeMess.children">
			<span v-for="(item,index) in treeMess.children">
				{{item.title}}
			</span>
            </div>
      </div>
</template>

<script>
    export default {
        name: '',
        data() {
            return {}
        },
        props: {
            dataMess: {
                type: Object,
                default: {}
            }
        },
        created() {

        },
        methods: {}
    }
</script>
