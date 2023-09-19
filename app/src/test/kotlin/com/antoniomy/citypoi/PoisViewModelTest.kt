package com.antoniomy.citypoi

import android.content.Context
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.datasource.local.LocalRepository
import com.antoniomy.domain.datasource.remote.RemoteRepository
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.Poi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PoisViewModelTest {
    private val localRepository: LocalRepository = mockk()
    private val remoteRepository: RemoteRepository = mockk()
    private val mPoi: Poi = mockk()
    private val viewModel = spyk(PoisViewModel(remoteRepository, localRepository))
    private val mContext: Context = mockk()

    @Test
    fun `When read the name of point of interest on local db and no exists in the database then the value of city return null`() =
       runTest {
            //Given
            val mName = "mPoi"

            coEvery { localRepository.readPoi(mName).value.city } returns null

            //When
            val insertPoi = localRepository.readPoi(mName)

            //Then
            Assert.assertEquals(insertPoi.value.city,null)

            coVerify { localRepository.readPoi(mName) }
        }

    @Test
    fun `When read the name of point of interest on local db and exists in the database then the value of city return name`() =
        runTest {
            //Given
            val mName = "mPoi"
            val city = "Madrid"

            coEvery { localRepository.readPoi(mName).value.city } returns city

            //When
            val insertPoi = localRepository.readPoi(mName)

            //Then
            Assert.assertEquals(insertPoi.value.city,city)

            coVerify { localRepository.readPoi(mName) }
        }


    @Test
    fun `When call to fetchPoiList then retrieve all point of interest stored on local db`() =
        runTest {
            //Given
            every { localRepository.fetchPoiList().value } returns listOf(Poi())

            //When
            val fetchPoiList = localRepository.fetchPoiList()

            //Then
            Assert.assertEquals(fetchPoiList.value, listOf(Poi()))

            verify { localRepository.fetchPoiList() }
        }


    @Test
    fun `When call to getDistrictList Then we receive a list of 30 POIs`() = runTest {
        //Given
        val mUrl = "https://FAKE_URL"
        every { remoteRepository.getDistrictList(mUrl).value.pois?.size } returns 30

        //When
        val getDistrictList = remoteRepository.getDistrictList(mUrl).value.pois?.size

        //Then
        assertEquals(30, getDistrictList)
    }

    @Test
    fun `When set a district tittle Then retrieve a tittle`() = runTest {
        //Given
        val mTittle = "Hello world!"
        every { viewModel.toolbarSubtitle } returns mTittle

        //When
        val mDistrictTittle = viewModel.toolbarSubtitle

        //Then
        assertEquals(mDistrictTittle, mTittle)
    }


    @Test
    fun `When call getMockedList Then we received the complete district`() = runTest {
        //Given
        val mDistrict = District()
        every { remoteRepository.getMockedList(mContext).value } returns mDistrict

        //When
        val fetchDistrict = remoteRepository.getMockedList(mContext).value

        //Then
        assertEquals(fetchDistrict, mDistrict)
    }

    @Test
    fun `When call getMockedList Then we received name of a district`() = runTest {
        //Given
        val mName = "JSON MODE"
        every { remoteRepository.getMockedList(mContext).value.name } returns mName

        //When
        val fetchDistrict = remoteRepository.getMockedList(mContext).value.name

        //Then
        assert(fetchDistrict == mName)
    }

    @Test
    fun `When call getMockedList Then we received the number of pois to our district`() = runTest {
        //Given
        val mPoiListSize = 6
        every { remoteRepository.getMockedList(mContext).value.pois?.size } returns mPoiListSize

        //When
        val fetchDistrict = remoteRepository.getMockedList(mContext).value.pois?.size

        //Then
        assertEquals(6, fetchDistrict)
    }


}