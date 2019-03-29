import Cookies from 'js-cookie';
import Vue from 'vue';

const app = {
    state: {
      user:null,
      listType:0,
      lineData:{}
    },
    mutations: {
      listTypeCh(state,data){
        state.listType = data
      },
      lineMess(state,data){
            state.lineData = data
        }
    }
};

export default app;
