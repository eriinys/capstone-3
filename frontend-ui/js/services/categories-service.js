let categoryService;

class CategoryService {


    getAllCategories(callback)
    {
        const url = `${config.baseUrl}/categories`;

        return axios.get(url)
            .then(response => {
              if (typeof callback === "function") {
                callback(response.data);
              }
            })
            .catch(error => {

            const status = error?.response?.status;
            const messageFromServer = error?.response?.data?.message;

            const msg = messageFromServer
            ? `Loading categories failed (${status}): ${messageFromServer}`
            : `Loading categories failed (${status || "no status"})`;

            templateBuilder.append("error", data, "errors")
            });
    }
}

document.addEventListener('DOMContentLoaded', () => {
    categoryService = new CategoryService();
});
