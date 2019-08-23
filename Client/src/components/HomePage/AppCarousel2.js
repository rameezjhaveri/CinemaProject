import React from 'react';
import AppCarousel21 from './AppCarousel21';
import Carousel from 'nuka-carousel';

const MoviePoster = (props) => {
  return(
    <div>
      <br></br>
      <h3>Now Playing</h3>
      <br></br>
      <Carousel slidesToShow={4} wrapAround pauseOnHover autoplayInterval={1500} autoplay={true} frameOverflow='hidden'>
        {
          props.movies.map((movie, i) => {
            return (
              <AppCarousel21 key={i} image={movie.poster_path} title={movie.title}/>
            )
          })
        }
      </Carousel>
    </div>
  );
}

export default MoviePoster;