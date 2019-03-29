import Cookies from 'js-cookie';
import Vue from 'vue';

const app = {
    state: {
        barNum:0,
        pjMess:{}
    },
    mutations: {
        barCh(state,data){
            state.barNum = data
        },
        pjMessCh(state,data){
          state.pjMess = data
        }
    }
};

export default app;
