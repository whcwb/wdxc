<template>
    <div>
        <template v-for="(item, index) in mesList">
            <div style="text-align: center;" :key="index">
                <Dropdown transfer placement="right-start" :key="index" @on-click="changeMenu">
                    <Button style="width: 70px;margin-left: -5px;padding:10px 0;" type="text">
                        <Icon :size="20" :color="iconColor" :type="item.icon"></Icon>
                    </Button>
                    <DropdownMenu style="" slot="list">
                        <template v-for="(child, i) in item.children">
                            <div v-if="child.children">
                                <Dropdown placement="right-start">
                                    <DropdownItem>
                                        <Icon :type="child.icon"></Icon>
                                        <span style="padding:0 15px 0 10px;">
                                          {{ itemTitle(child) }}
                                        </span>
                                        <Icon type="arrow-right-b"></Icon>
                                    </DropdownItem>
                                    <DropdownMenu slot="list">
                                        <DropdownItem v-for = "childs in child.children" :name="childs.name">
                                            <Icon :type="childs.icon"></Icon>
                                            <span style="padding-left:10px;">
                                                {{ itemTitle(childs) }}
                                              </span>
                                        </DropdownItem>
                                    </DropdownMenu>
                                </Dropdown>
                            </div>
                            <div v-else>
                                <DropdownItem :name="child.name" :key="i">
                                    <Icon :type="child.icon"></Icon>
                                    <span style="padding-left:10px;">
                                      {{ itemTitle(child) }}
                                    </span>
                                </DropdownItem>
                            </div>
                        </template>
                    </DropdownMenu>
                </Dropdown>
            </div>
        </template>
    </div>
</template>

<script>
    import menuList from '../../../../data/list'
    export default {
        name: 'sidebarMenuShrink',
        data(){
            return {
                menuNmae:'',
                mesList: []
            }
        },
        props: {
            menuList: {
                type: Array
            },
            iconColor: {
                type: String,
                default: 'white'
            },
            menuTheme: {
                type: String,
                default: 'darck'
            }
        },
        created() {
//          this.mesList = menuList.menuTree
            this.mesList = this.session.getItem('menuList')
            log('菜单数据1',this.session.getItem('menuList'))
        },
        methods: {
            //菜单点击事件
            changeMenu (active) {
                log('caidan',active);
//              this.$store.commit('setCurrentPageName',active)
                this.$emit('on-change', active);
            },
            itemTitle (item) {
                if (typeof item.title === 'object') {
                    return this.$t(item.title.i18n);
                } else {
                    return item.title;
                }
            }
        }
    };
</script>
