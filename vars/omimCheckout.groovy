def call(credentials, branch, refspec=null, isPRBuilder=null) {
    def DEFAULT_REF = '+refs/heads/*:refs/remotes/origin/*'
    def PR_REF = '+refs/pull/*:refs/remotes/origin/pr/*'
    def TAG_REF = '+refs/tags/*:refs/remotes/origin/tags/*'
    
    def refs = DEFAULT_REF
    if (refspec == 'pullRequest') {
        branch = "origin/pr/${branch}/head"
        refs = PR_REF
    }
    if (refspec == 'tag') {
        refs = TAG_REF
    }
    if (isPRBuilder != null) {
        branch = isPRBuilder
        refs = PR_REF
    }
    dir('omim') {
        checkout([$class: 'GitSCM',
            branches: [[name: branch]],
            doGenerateSubmoduleConfigurations: false,
            extensions:
                [[$class: 'CleanCheckout'],
                [$class: 'CloneOption', 
                    depth: 0, 
                    noTags: false,
                    shallow: false,
                    timeout: 90],
                [$class: 'SubmoduleOption',
                    disableSubmodules: false, 
                    parentCredentials: false,
                    recursiveSubmodules: true,
                    trackingSubmodules: false]],
                submoduleCfg: [],
                userRemoteConfigs: [[credentialsId: credentials,
                    url: 'https://github.com/mapsme/omim',
                    refspec: refs]]
        ]);
    }
}
