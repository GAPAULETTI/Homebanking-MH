Vue.createApp({
    data() {
        return {
            clientInfo: {},
            selectedAccount: [],
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData: function () {
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client info
                    this.clientInfo = response.data;
                    console.log(this.clientInfo)
                    console.log(this.clientInfo.accountsDTO)
                    this.selectAccount = this.clientInfo.accountsDTO.map(account => account.accountType)


                })
                .catch((error) => {
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate: function (date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function () {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(() => {
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
        deleteAccount: function(numberAccount){
                //axios.patch("/api/clients/current/accounts?", `accountNumber=${this.numberAccount}`)
                axios.patch(`/api/clients/current/accounts?accountNumber=${numberAccount}`)
                .then( response =>{
                this.modal.show();
                })
                 .catch((error) => {
                    console.log(error);
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })


        },
        finish: function () {
                            window.location.reload();
        },
        create: function () {
            axios.post(`/api/clients/current/accounts?accountType=${this.selectedAccount}`)
                .then(response => {
                this.createModal.hide();
                window.location.reload();
                })
                .catch((error) => {
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })
        },
         createAccount: function(){
                    this.createModal.show();
         },
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.modal = new bootstrap.Modal(document.getElementById('deleteModal'));
        this.createModal = new bootstrap.Modal(document.getElementById('selectAccountType'));
        this.getData();
    }
}).mount('#app')