<template>
    <Menu theme="light" :accordion="true" style="height: 500px;">
        <Submenu v-for="(item1,index1) in data" :key="item1.gndm" :name="item1.gndm">
            <template slot="title">
                <Checkbox :ref="item1.gndm+'>'" :label="item1.gndm" :value="checkBoxModels[item1.gndm]"   @on-change="(b)=>itemChange(b,item1.gndm+'>')">
                    <span>{{item1.gnmc}}</span>
                </Checkbox>
            </template>
            <div v-if="item1.children && item1.children.length > 0" v-for="(item2,index2) in item1.children">
                <MenuItem v-if="!item2.children || item2.children.length <= 0" :name="item2.gndm">
                    <Checkbox :ref="item1.gndm+'>'+item2.gndm" :label="item2.gndm" :value="checkBoxModels[item2.gndm]"  @on-change="(b)=>itemChange(b,item1.gndm+'>'+item2.gndm)">
                        <span>{{item2.gnmc}}</span>
                    </Checkbox>
                </MenuItem>
                <Submenu v-else :name="item2.gndm">
                    <template slot="title">
                        <Checkbox :ref="item1.gndm+'>'+item2.gndm+'>'" :label="item2.gndm" :value="checkBoxModels[item2.gndm]"   @on-change="(b)=>itemChange(b,item1.gndm+'>'+item2.gndm+'>')">
                            <span>{{item2.gnmc}}</span>
                        </Checkbox>
                    </template>
                    <MenuItem v-for="(item3,index3) in item2.children" :name="item3.gndm">
                        <Checkbox :ref="item1.gndm+'>'+item2.gndm+'>'+item3.gndm" :label="item3.gndm" :value="checkBoxModels[item3.gndm]"   @on-change="(b)=>itemChange(b,item1.gndm+'>'+item2.gndm+'>'+item3.gndm)">
                            <span>{{item3.gnmc}}</span>
                        </Checkbox>
                    </MenuItem>
                </Submenu>
            </div>
        </Submenu>
    </Menu>
</template>

<script>
    export default {
        name: 'menuChoose',
        data() {
            return {
                choosedIds:[],
                checkBoxModels:{},
            }
        },
        props: {
            data: {
                type: Array,
                default: function () {
                    return []
                }
            },
            choosedData: {
                type: Array,
                default: function () {
                    return []
                }
            }
        },
        mounted() {
        },
        created() {
             this.choosedIds = this.choosedData;
             this.initModels(this.data);
        },
        methods: {
            initModels(list){
                for(let r of list){
                    this.checkBoxModels[r.gndm] = this.choosedIds.indexOf(r.gndm) >= 0;
                    r.children && this.initModels(r.children);
                }
            },
            // 判断是否有子节点
            hasChildren(ref){
                return ref.charAt(ref.length - 1) == '>';
            },
            hasParents(ref){
                return ref.split('>')[1] != '';
            },
            itemChange(check,ref){
                check ? this.itemCheck(ref) : this.itemCancel(ref);
                this.checkBoxModels[this.$refs[ref][0].label] = check;
                let choosedIds = [];
                for (let r in this.checkBoxModels){
                    if (this.checkBoxModels[r]){
                        choosedIds.push(r);
                    }
                }
                this.$emit('treeChange',choosedIds);
            },
            itemCheck(ref){
                this.hasChildren(ref) && this.checkChildren(ref);
                this.hasParents(ref)  && this.checkParents(ref);
            },
            checkChildren(ref){
                let list = this.getChildren(ref);
                this.changeListStatus(list,true);
            },
            checkParents(ref){
                let list = this.getParents(ref);
                this.changeListStatus(list,true);
            },
            changeListStatus(list,check){
                let newStatusList = this.checkBoxModels;
                for(let r of list){
                    newStatusList[r[0].label] = check;
                }
                this.checkBoxModels = {};
                this.checkBoxModels = newStatusList;
            },
            itemCancel(ref){
                this.hasChildren(ref) && this.cancelChildren(ref);
            },
            cancelChildren(ref){
                let list = this.getChildren(ref);
                this.changeListStatus(list,false);
            },
            cancelParent(ref){
                let list = this.getParents(ref);
                this.changeListStatus(list,false);
            },
            getChildren(ref){
                let list = [];
                for (let r in this.$refs){
                    if (r.indexOf(ref) >= 0){
                        list.push(this.$refs[r]);
                    }
                }
                return list;
            },
            getParents(ref){
                let list = [];
                for (let r in this.$refs){
                    if (ref.indexOf(r) >= 0){
                        list.push(this.$refs[r]);
                    }
                }
                return list;
            }
        }
    }
</script>

<style>

</style>
