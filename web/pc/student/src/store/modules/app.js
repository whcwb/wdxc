import Cookies from 'js-cookie';
import Vue from 'vue';

const app = {
    state: {
        barNum:0,
        lineID:0,
    },
    mutations: {
        barCh(state,data){
            state.barNum = data
        },
        ChlineID(state,data){
              state.lineID = data
        }
    }
};

export default app;
