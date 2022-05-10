import axios from 'axios'

export const AXIOS = axios.create({ 
     baseURL: `http://${window.location.hostname}:8080/api` 
})