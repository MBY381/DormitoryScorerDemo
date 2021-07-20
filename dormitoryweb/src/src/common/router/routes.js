const homechild = [
    {
        path:'/user/sys_admin',
        name:'userSys_admin',
        component: () => import('@/views/user/sys_admin.vue')
    },
    {
        path:'/user/student',
        name:'userStudent',
        component: () => import('@/views/user/student.vue')
    },
    {
        path:'/user/admin',
        name:'userAdmin',
        component: () => import('@/views/user/admin.vue')
    },
    {
        path:'/user/repairer',
        name:'userRepairer',
        component: () => import('@/views/user/repairer.vue')
    },
    {
        path:'/user/deliever',
        name:'userdeliever',
        component: () => import('@/views/user/deliever.vue')
    },
    {
        path:'/user/teacher',
        name:'userteacher',
        component: () => import('@/views/user/teacher.vue')
    },
    {
        path:'/user/scorer',
        name:'userscorer',
        component: () => import('@/views/user/scorer.vue')
    },
];

const routes = [

    {
        path: '/',
        redirect: '/security/login'
    },
    {
        path: '/security/login',
        name: 'Login',
        component: () => import('@/views/security/login.vue')
    },
    {
        path:'/security/register',
        name:'Register',
        component: () => import('@/views/security/register.vue')
    },
    {
        path:'/security/changePwd',
        name:'changePwd',
        component: () => import('@/views/security/changePwd.vue')
    },
    {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/home/home.vue'),
        children:homechild
    },
    {
        path: '/404',
        name: "NotFound",
        component: () => import('@/views/404.vue')
    },
    {
        path:'/security/complete',
        name:'securityComplete',
        component: () => import('@/views/security/complete.vue')
    },

];

export const existsRoute = (path) => {
    for (let i = 0; i < routes.length; i++) {
        const r = routes[i];
        if (r.path == path) {
            return true;
        }
        if (r.children && r.children.length) {
            for (let k = 0; k < r.children.length; k++) {
                const sr = r.children[k];
                if (sr.path == path) {
                    return true;
                }
            }
        }
    }
    return false;
};

export default routes;