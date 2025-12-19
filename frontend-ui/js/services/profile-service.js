let profileService;

class ProfileService
{
    loadProfile()
    {
        const url = `${config.baseUrl}/profile`;

        axios.get(url)
             .then(response => {
                 //stores user's saved default currency across the app
                 window.defaultCurrency = response.data.defaultCurrency || "USD";

                 templateBuilder.build("profile", response.data, "main");
                 //update profile to show saved currency preference
                 const select = document.getElementById("defaultCurrency");
                 if (select) {
                     select.value = response.data.defaultCurrency ?? "USD";
                 }
             })
             .catch(error => {
                 const data = {
                     error: "Load profile failed."
                 };

                 templateBuilder.append("error", data, "errors")
             })
    }

    loadDefaultCurrencyOnly()
    {
            const url = `${config.baseUrl}/profile`;

            axios.get(url)
                .then(response => {
                    window.defaultCurrency = response.data.defaultCurrency || "USD";
                })
                .catch(() => {
                    window.defaultCurrency = "USD";
                });
    }


    updateProfile(profile)
    {

        const url = `${config.baseUrl}/profile`;

        axios.put(url, profile)
             .then(() => {
                 const data = {
                     message: "The profile has been updated."
                 };

                 templateBuilder.append("message", data, "errors")
             })
             .catch(error => {
                 const data = {
                     error: "Save profile failed."
                 };

                 templateBuilder.append("error", data, "errors")
             })
    }
}

document.addEventListener("DOMContentLoaded", () => {
   profileService = new ProfileService();
});
