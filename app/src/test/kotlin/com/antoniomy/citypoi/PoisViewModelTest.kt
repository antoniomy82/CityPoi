package com.antoniomy.citypoi

import com.antoniomy.domain.datasource.local.LocalRepository
import com.antoniomy.domain.datasource.remote.RemoteRepository
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.Poi
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
class PoisViewModelTest {

    private val localRepository: LocalRepository = mockk()
    private val remoteRepository: RemoteRepository = mockk()
    private val mPoi: Poi = mockk()

    @Test
    fun `When insert point of interest on local db correctly then insertPoi return true`() =
        runTest {
            //Given
            every { localRepository.insertPoi(mPoi) } returns true

            //When
            val insertPoi = localRepository.insertPoi(mPoi)

            //Then
            Assert.assertTrue(insertPoi)

            verify { localRepository.insertPoi(mPoi) }
        }

    @Test
    fun `When insert point of interest on local db wrongly then insertPoi return false`() =
        runTest {
            //Given
            every { localRepository.insertPoi(mPoi) } returns false

            //When
            val insertPoi = localRepository.insertPoi(mPoi)

            //Then
            Assert.assertFalse(insertPoi)

            verify { localRepository.insertPoi(mPoi) }
        }

    @Test
    fun `When delete point of interest on local db correctly then deletePoi return true`() =
        runTest {
            //Given
            every { localRepository.deletePoi("NAME") } returns true

            //When
            val deletePoi = localRepository.deletePoi("NAME")

            //Then
            Assert.assertTrue(deletePoi)

            verify { localRepository.deletePoi("NAME") }
        }

    @Test
    fun `When delete point of interest on local db wrongly then deletePoi return false`() =
        runTest {
            //Given
            every { localRepository.deletePoi("NAME") } returns false

            //When
            val deletePoi = localRepository.deletePoi("NAME")

            //Then
            Assert.assertFalse(deletePoi)

            verify { localRepository.deletePoi("NAME") }
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
    fun `Given a correct Mocked Json list when call getMockedList Then we received the pois of a district`()= runTest {
        val mName = "JSON MODE"
       // val mContext = mock(Context::class.java)
       every { remoteRepository.getMockedList(mockk()).value } returns District()
    }

    @Test
    fun testSum() {
        val expected = 6
        assertEquals(expected, 4+2)
    }


}