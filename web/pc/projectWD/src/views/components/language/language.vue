<template>
      <div>
            <Dropdown trigger="click" @on-click="selectLang">
                  <a href="javascript:void(0)">
                        {{ title }}
                        <Icon :size="18" type="md-arrow-dropdown"/>
                  </a>
                  <DropdownMenu slot="list">
                        <DropdownItem v-for="(value, key) in localList" :name="key" :key="`lang-${key}`">{{ value }}
                        </DropdownItem>
                  </DropdownMenu>
            </Dropdown>
      </div>
</template>

<script>
    import {mapMutations} from 'vuex'

    export default {
        name: 'Language',
        // props: {
        //     lang: String
        // },
        data() {
            return {
                langList: {
                    'zh-CN': '语言',
                    // 'zh-TW': '語言',
                    'en-US': 'Lang'
                },
                localList: {
                    'zh-CN': '中文简体',
                    // 'zh-TW': '中文繁体',
                    'en-US': 'English'
                }
            }
        },
        computed:{
            lang:function () {
                return this.$store.state.app.local
            }
        },
        watch: {
            lang:function(n,o) {
                console.log('111=====',n);
                this.$i18n.locale = n
            }
        },
        computed: {
            title() {
                return this.langList[this.lang]
            }
        },
        methods: {
            selectLang(name) {
                console.log('selectLang',name);
                this.$i18n.locale = name
                this.$store.commit('setLocal', name)
            }
        }
    }
</script>
