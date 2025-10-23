import request from '@/utils/request';

export default {
    projects(data) {
        return request({
            url: "/admin/application/page",
            method: "post",
            data: data
        });
    },
    add(data) {
        return request({
            url: "/admin/application",
            method: "post",
            data: data
        });
    },
    overview(appId) {
        return request({
            url: "/admin/application/overview?id=" + appId,
            method: "get",
        });
    },
    update(appId, data) {
        return request({
            url: "/admin/application/update/" + appId,
            method: "patch",
            data: data
        });
    },
    delete(appId) {
        return request({
            url: "/admin/application/" + appId,
            method: "delete",
        });
    }
}