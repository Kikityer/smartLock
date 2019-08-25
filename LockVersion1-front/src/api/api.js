/**
 *Created by HDH on 2018/9/7.
 */
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8081';

export const requestLogin =  params => {return axios.post('/manage/login',params).then(res => res.data)};
export const getUserListPage = params => { return axios.get('/user/listpage', { params: params }); };
export const removeUser = params => { return axios.get('/user/remove', { params: params }); };//删除数据库中用户信息
export const batchRemoveUser = params => { return axios.get('/user/batchremove', { params: params }); };//批量删除数据库中用户信息
export const editUser = params => { return axios.get('/user/edit', { params: params }); };//修改数据库中用户的信息
export const addUser = params => { return axios.get('/user/add', { params: params }); };//向数据库中添加用户信息
