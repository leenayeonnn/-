## setting
setRepositories(ind = 1:7)

# install.packages("rvest")
# install.packages("RSelenium")
# install.packages("tidyverse")
# install.packages("janitor")
# install.packages("gganimate")
# install.packages("gifski")
# install.packages("writexl")

library(rvest)
library(RSelenium)
library(tidyverse)
library(janitor)
library(writexl)
WORK_DIR <- "C:\\UNIV_Lecture\\CD"
setwd(WORK_DIR)
getwd()

## 접속
rD <- remoteDriver(remoteServerAddr="localhost", port=4445L, browserName="chrome")
rD$open()
rD$setWindowSize(1920, 1080)

total_Data <- data.frame()

## 여러 주소 바꿔가면서 데이터 수집
URL <- "https://www.melon.com/mymusic/dj/mymusicdjplaylistview_inform.htm?plylstSeq=526061577"
rD$navigate(URL) 

Sys.sleep(1)

page_source <- rD$getPageSource()

song <- read_html(page_source[[1]]) %>%
        html_nodes('.ellipsis.rank01') %>% 
        html_text()

song <- stringr::str_replace_all(song, '[\t\n\r]', '')

singer <- read_html(page_source[[1]]) %>%
            html_nodes('.ellipsis.rank02') %>% 
            html_text()

singer <- stringr::str_replace_all(singer, '[\t\n\r]', '')
singer <- substring(singer, 1, nchar(singer) / 2)

tmp <- bind_cols(song, singer)

total_Data <- bind_rows(tmp, total_Data)

total_Data <- unique(total_Data)

sad <- total_Data

######################
col <- c("song", "singer")

names(sad) <- col
names(happy) <- col
names(mad) <- col


mad %>%
  mutate(song = gsub("19금", "", song)) -> mad
happy %>%
  mutate(song = gsub("19금", "", song)) -> happy
sad %>%
  mutate(song = gsub("19금", "", song)) -> sad

write_xlsx(mad, path = "mad.xlsx")
write_xlsx(sad, path = "sad.xlsx")
write_xlsx(happy, path = "happy.xlsx")
