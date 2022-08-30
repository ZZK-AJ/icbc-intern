import { createStore } from 'vuex'

const TOKEN_KEY = "token"
const USER_KEY = "user"

interface User {
  userName: string;
  userId: string;
  token: string;
  last: string;
}

let loginUser: User|null = null;

const userJson = window.localStorage.getItem(USER_KEY);
if (userJson != null) {
  try {
    loginUser = JSON.parse(userJson);
  } catch {

  }
}


export default createStore({
  state: {
    user: loginUser,
    token: window.localStorage.getItem(TOKEN_KEY),
    currentIndex: Number(window.sessionStorage.getItem("index")),
  },
  getters: {
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
      window.localStorage.setItem(TOKEN_KEY, token);
    },
    setUser(state, user: User) {
      state.user = user;
      window.localStorage.setItem(USER_KEY, JSON.stringify(user));
    },
    setCurrentIndex(state, currentIndex) {
      state.currentIndex = currentIndex;
      window.sessionStorage.setItem("index", currentIndex);
    }
  },
  actions: {
  },
  modules: {
  }
})
