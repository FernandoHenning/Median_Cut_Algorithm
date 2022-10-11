# Median Cut Algortigm
Median cut is an algorithm to sort data of an arbitrary number of dimensions into series of sets by recursively cutting each set of data at the median point along the longest dimension. Median cut is typically used for color quantization. [[1]](https://en.wikipedia.org/wiki/Median_cut#:~:text=Median%20cut%20is%20an%20algorithm,typically%20used%20for%20color%20quantization.)  
## How does it work
Suppose we have an image with an arbitrary number of pixels and want to generate a palette of 16 colors. 
1. Put all the pixels of the image (that is, their RGB values) in a bucket.
2. Find out which color channel (red, green, or blue) among the pixels in the bucket has the greatest range.
3. Sort the pixels according to that channel's values.
4. Move the upper half of the pixels into a new bucket. (It is this step that gives the median cut algorithm its name; the buckets are divided into two at the median of the list of pixels.)

Repeat the process on both buckets, giving you 4 buckets, then repeat on all 4 buckets, giving you 8 buckets, then repeat on all 8, giving you 16 buckets. Average the pixels in each bucket and you have a palette of 16 colors.
Since we are dividing the cube into two cubes, the number of colors an image can be reduced to is $2^n$

## Examples
### Original
![original](https://user-images.githubusercontent.com/61071974/195164702-9c2049f5-03cc-4b09-b35d-21c42ff731a8.jpg)
Original             |  $2^n$ colors
:-------------------------:|:-------------------------:
![original](https://user-images.githubusercontent.com/61071974/195164702-9c2049f5-03cc-4b09-b35d-21c42ff731a8.jpg) Original Image | ![two_colors_image](https://user-images.githubusercontent.com/61071974/195165021-b030e9fd-ba03-4f61-8b05-10c1f9ff49fa.jpg) Number of colors: 2
![original](https://user-images.githubusercontent.com/61071974/195164702-9c2049f5-03cc-4b09-b35d-21c42ff731a8.jpg) Original Image | ![Uploading four_colors_image.jpg…]() Number of colors: 4



