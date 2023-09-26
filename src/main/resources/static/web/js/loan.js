    Vue.createApp({
        data(){
            return{
                loanInfo: {},
                loan: {},
                errorToats: null,
                errorMsg: null,
            }
        },
        methods:{
            getData: function(){
                const urlParams = new URLSearchParams(window.location.search);
                console.log(urlParams)
                const id = urlParams.get('id');
                //axios.get(`/api/clients/current/loans/${id}`)
                axios.get("/api/clients/current/loans?", `${id}`)
                    .then((response) => {
                        console.log(response)
                        this.loanInfo = response.data;
                        this.loan = this.loanInfo.find(loan => loan.id == id);
                        console.log(this.loan)

                    })
                     .catch((error) => {
                                        // handle error
                                        this.errorMsg = "Error getting data";
                                        this.errorToats.show();
                    })
            },
            signOut: function () {
                        axios.post('/api/logout')
                            .then(response => window.location.href = "/web/index.html")
                            .catch(() => {
                                this.errorMsg = "Sign out failed"
                                this.errorToats.show();
                            })
            },
        },
        mounted: function () {
                this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
                this.getData();
            }
    }).mount('#app')

