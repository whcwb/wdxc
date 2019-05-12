// import { mapState } from 'vuex'
export default {
    created(){
    },
    computed:{
        // ...mapState({
        //     local:state => state.app.local
        // })
    },
    watch:{
        // local:function (n,o) {
        //     this.GetTabTitLang()
        // }
    },
    mounted() {
        this.$nextTick(()=>{
            try {
                this.GetTabTitLang()
                // this.tab_H = this.AF.getDom_H(this.tabBox)
            }catch (e) {
            }
        })
    },
    methods: {
        GetTabTitLang() {
            this.tableTiT.forEach((it, index) => {
                it.title = this.$t(it.tit)
            })
        }
    }
}