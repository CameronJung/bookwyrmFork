<template>
  <div class="book_search">
    <div class="row justify-content-center">
      <div class="col-md-6 text-align-center card foreground btn highlight p-3 mt-4" @click="$router.push('/create')">
        <h3>create</h3>
      </div>
    </div>
    <div class="row justify-content-center">
      <div id="bookList" class="col-6 " v-if="this.bookList.length != 0">
        <div id="bookItem" v-for="book in bookList" :key="book.id">
          <BookBriefComponent class="m-3 highlight" :title="book.title"  :author="book.author" :bookId="book.id"/>
        </div>
      </div>
      <div v-else class="card col-6 justify-content-center foreground m-3 p-1 text-center">
        <h2>We couldn't find that book</h2>
        <p>
          Please ensure your spelling is correct and try again.
          Alternatively, you could add the book using the link above.
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import BookService from "@/services/BookService"
import BookBriefComponent from "@/components/BookBriefComponent"
export default {
  name: 'Book Search',
  data(){
    return {
      bookList : []
    }
  },
  watch:{
    '$route.params.searchTerm' (){
      this.searchForBooks(this.$route.params.searchTerm);
    }
  },
  created(){
    this.searchForBooks(this.$route.params.searchTerm);
  },
  methods:{
    searchForBooks(searchTerm){
      BookService.searchBook(searchTerm).then(response => this.bookList = response.data.bookDaoList).catch(() => this.bookList = []);
    }
  },
  components: {BookBriefComponent}
}
</script>
