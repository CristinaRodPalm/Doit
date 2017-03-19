(function () {
    'use strict';

    angular
        .module('doitApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('user-ext', {
                parent: 'entity',
                url: '/user-ext',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserExts'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/user-ext/user-exts.html',
                        controller: 'UserExtController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {}
            })
            .state('user-ext-detail', {
                parent: 'user-ext',
                url: '/user-ext/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserExt'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/user-ext/user-ext-detail.html',
                        controller: 'UserExtDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'UserExt', function ($stateParams, UserExt) {
                        return UserExt.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'user-ext',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('user-ext-detail.edit', {
                parent: 'user-ext-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/user-ext/user-ext-dialog.html',
                        controller: 'UserExtDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['UserExt', function (UserExt) {
                                return UserExt.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('user-ext.new', {
                parent: 'user-ext',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/user-ext/user-ext-dialog.html',
                        controller: 'UserExtDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    fechaNacimiento: null,
                                    imagen: null,
                                    imagenContentType: null,
                                    telefono: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('user-ext', null, {reload: 'user-ext'});
                    }, function () {
                        $state.go('user-ext');
                    });
                }]
            })
            .state('user-ext.edit', {
                parent: 'user-ext',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/user-ext/user-ext-dialog.html',
                        controller: 'UserExtDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['UserExt', function (UserExt) {
                                return UserExt.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('user-ext', null, {reload: 'user-ext'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('user-ext.delete', {
                parent: '',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/user-ext/user-ext-delete-dialog.html',
                        controller: 'UserExtDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['UserExt', function (UserExt) {
                                return UserExt.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('user-ext', null, {reload: 'user-ext'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('user-search', {
                parent: 'user-ext',
                url: '/search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Buscador'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/user-ext/user-ext-search.html',
                        controller: 'UserExtController',
                        controllerAs: 'vm'
                    }
                }
            });
    }

})();
