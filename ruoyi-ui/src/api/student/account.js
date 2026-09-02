import request from '@/utils/request'

// 查询学生账号列表
export function listAccount(query) {
  return request({
    url: '/student/account/list',
    method: 'get',
    params: query
  })
}

// 查询学生账号详细
export function getAccount(id) {
  return request({
    url: '/student/account/' + id,
    method: 'get'
  })
}

// 新增学生账号
export function addAccount(data) {
  return request({
    url: '/student/account',
    method: 'post',
    data: data
  })
}

// 修改学生账号
export function updateAccount(data) {
  return request({
    url: '/student/account',
    method: 'put',
    data: data
  })
}

// 删除学生账号
export function delAccount(id) {
  return request({
    url: '/student/account/' + id,
    method: 'delete'
  })
}
