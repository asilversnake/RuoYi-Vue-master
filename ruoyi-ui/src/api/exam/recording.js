import request from '@/utils/request'

// 查询考试记录列表
export function listRecording(query) {
  return request({
    url: '/exam/recording/list',
    method: 'get',
    params: query
  })
}

// 查询考试记录详细
export function getRecording(id) {
  return request({
    url: '/exam/recording/' + id,
    method: 'get'
  })
}

// 新增考试记录
export function addRecording(data) {
  return request({
    url: '/exam/recording',
    method: 'post',
    data: data
  })
}

// 修改考试记录
export function updateRecording(data) {
  return request({
    url: '/exam/recording',
    method: 'put',
    data: data
  })
}

// 删除考试记录
export function delRecording(id) {
  return request({
    url: '/exam/recording/' + id,
    method: 'delete'
  })
}
